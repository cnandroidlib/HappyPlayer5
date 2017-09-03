package com.zlm.hp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zlm.hp.application.HPApplication;
import com.zlm.hp.db.AudioInfoDB;
import com.zlm.hp.db.DownloadThreadDB;
import com.zlm.hp.libs.utils.ColorUtil;
import com.zlm.hp.libs.utils.ToastUtil;
import com.zlm.hp.libs.widget.CircleImageView;
import com.zlm.hp.manager.AudioPlayerManager;
import com.zlm.hp.model.AudioInfo;
import com.zlm.hp.model.AudioMessage;
import com.zlm.hp.receiver.AudioBroadcastReceiver;
import com.zlm.hp.ui.R;
import com.zlm.hp.utils.ImageUtil;
import com.zlm.hp.widget.IconfontTextView;
import com.zlm.hp.widget.ListItemRelativeLayout;

import java.util.List;

/**
 * 主界面当前播放列表
 */
public class MainPopPlayListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 无数据
     */
    public static final int NODATA = 1;
    /**
     * 已经到底了
     */
    public static final int NOMOREDATA = 2;
    /**
     * 内容item
     */
    public static final int OTHER = 4;
    private Context mContext;
    private List<AudioInfo> mDatas;
    private int state = NOMOREDATA;

    /**
     * 播放歌曲索引
     */
    private int playIndexPosition = -1;
    private String playIndexHash = "-1";
    private HPApplication mHPApplication;


    public MainPopPlayListAdapter(HPApplication hPApplication, Context context, List<AudioInfo> datas) {
        this.mHPApplication = hPApplication;
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        if (state == NODATA && mDatas.size() == position) {
            return NODATA;
        } else {
            if (mDatas.size() == position) {
                return NOMOREDATA;
            }
            return OTHER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = null;
        if (viewType == NODATA) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_lvitem_nodata, null, false);
            NoDataViewHolder holder = new NoDataViewHolder(view);
            return holder;
        } else if (viewType == NOMOREDATA) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_lvitem_nomoredata, null, false);
            NoDataViewHolder holder = new NoDataViewHolder(view);
            return holder;
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_lvitem_main_popsong, null, false);
            PopListViewHolder holder = new PopListViewHolder(view);
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof PopListViewHolder && position < mDatas.size()) {
            AudioInfo audioInfo = mDatas.get(position);
            reshViewHolder(position, (PopListViewHolder) viewHolder, audioInfo);
        }
    }

    /**
     * 刷新
     *
     * @param position
     * @param viewHolder
     * @param audioInfo
     */
    private void reshViewHolder(final int position, final PopListViewHolder viewHolder, final AudioInfo audioInfo) {
        //
        //
        int downloadSize = DownloadThreadDB.getDownloadThreadDB(mContext).getDownloadedSize(audioInfo.getHash());
        if (audioInfo.getType() == AudioInfo.LOCAL || downloadSize >= audioInfo.getFileSize()) {
            viewHolder.getIslocalImg().setVisibility(View.VISIBLE);
        } else {
            viewHolder.getIslocalImg().setVisibility(View.GONE);
        }
        //判断是否是喜欢歌曲
        boolean isLike = AudioInfoDB.getAudioInfoDB(mContext).isRecentOrLikeExists(audioInfo.getHash(), audioInfo.getType(), false);
        if (isLike) {
            viewHolder.getLikedImg().setVisibility(View.VISIBLE);
            viewHolder.getUnLikeTv().setVisibility(View.INVISIBLE);
        } else {
            viewHolder.getLikedImg().setVisibility(View.INVISIBLE);
            viewHolder.getUnLikeTv().setVisibility(View.VISIBLE);
        }
        viewHolder.getLikedImg().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.getLikedImg().setVisibility(View.INVISIBLE);
                viewHolder.getUnLikeTv().setVisibility(View.VISIBLE);
                ToastUtil.showTextToast(mContext, "取消成功");

                //删除喜欢歌曲
                Intent delIntent = new Intent(AudioBroadcastReceiver.ACTION_LIKEDELETE);
                delIntent.putExtra(AudioInfo.KEY, audioInfo);
                delIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                mContext.sendBroadcast(delIntent);
            }
        });
        viewHolder.getUnLikeTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.getLikedImg().setVisibility(View.VISIBLE);
                viewHolder.getUnLikeTv().setVisibility(View.INVISIBLE);
                ToastUtil.showTextToast(mContext, "已添加收藏");

                //添加喜欢歌曲
                Intent addIntent = new Intent(AudioBroadcastReceiver.ACTION_LIKEADD);
                addIntent.putExtra(AudioInfo.KEY, audioInfo);
                addIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                mContext.sendBroadcast(addIntent);
            }
        });

        //判断当前索引
        if (audioInfo.getHash().equals(mHPApplication.getPlayIndexHashID())) {
            playIndexPosition = position;
            playIndexHash = mHPApplication.getPlayIndexHashID();
            //
            viewHolder.getSongIndexTv().setVisibility(View.INVISIBLE);
            viewHolder.getSingPicImg().setVisibility(View.VISIBLE);
            //
            viewHolder.getSingerNameTv().setTextColor(ColorUtil.parserColor("#0288d1"));
            viewHolder.getSongNameTv().setTextColor(ColorUtil.parserColor("#0288d1"));

            //加载歌手图片
            ImageUtil.loadSingerImage(mHPApplication, mContext, viewHolder.getSingPicImg(), audioInfo.getSingerName());


        } else {
            viewHolder.getSongIndexTv().setVisibility(View.VISIBLE);
            viewHolder.getSingPicImg().setVisibility(View.INVISIBLE);
            //
            viewHolder.getSingerNameTv().setTextColor(ColorUtil.parserColor("#555555"));
            viewHolder.getSongNameTv().setTextColor(Color.BLACK);
        }

        //显示歌曲索引
        viewHolder.getSongIndexTv().setText(((position + 1) < 10 ? "0" + (position + 1) : (position + 1) + ""));

        String singerName = audioInfo.getSingerName();
        String songName = audioInfo.getSongName();

        viewHolder.getSongNameTv().setText(songName);
        viewHolder.getSingerNameTv().setText(singerName);
        //item点击事件
        viewHolder.getListItemRelativeLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (playIndexPosition == position) {
                    if (mHPApplication.getPlayStatus() == AudioPlayerManager.PLAYING) {
                        // 当前正在播放，发送暂停

                        Intent pauseIntent = new Intent(AudioBroadcastReceiver.ACTION_PAUSEMUSIC);
                        pauseIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                        mContext.sendBroadcast(pauseIntent);

                        return;
                    } else if (mHPApplication.getPlayStatus() == AudioPlayerManager.PAUSE) {
                        //当前正在暂停，发送唤醒播放

                        Intent remuseIntent = new Intent(AudioBroadcastReceiver.ACTION_RESUMEMUSIC);
                        remuseIntent.putExtra(AudioMessage.KEY, mHPApplication.getCurAudioMessage());
                        remuseIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                        mContext.sendBroadcast(remuseIntent);

                        return;
                    }
                }

                //设置界面ui
                viewHolder.getSongIndexTv().setVisibility(View.INVISIBLE);
                viewHolder.getSingPicImg().setVisibility(View.VISIBLE);
                //
                viewHolder.getSingerNameTv().setTextColor(ColorUtil.parserColor("#0288d1"));
                viewHolder.getSongNameTv().setTextColor(ColorUtil.parserColor("#0288d1"));
                //加载歌手图片
                ImageUtil.loadSingerImage(mHPApplication, mContext, viewHolder.getSingPicImg(), audioInfo.getSingerName());


                //
                if (playIndexPosition != -1) {
                    notifyItemChanged(playIndexPosition);
                }

                //
                playIndexPosition = position;
                playIndexHash = audioInfo.getHash();
                mHPApplication.setPlayIndexHashID(playIndexHash);

                //发送播放广播
                Intent playIntent = new Intent(AudioBroadcastReceiver.ACTION_PLAYMUSIC);
                AudioMessage audioMessage = new AudioMessage();
                audioMessage.setAudioInfo(audioInfo);
                playIntent.putExtra(AudioMessage.KEY, audioMessage);
                playIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                mContext.sendBroadcast(playIntent);
            }
        });
    }


    /**
     * 刷新view
     *
     * @param audioInfo
     */
    public void reshViewHolder(AudioInfo audioInfo) {
        if (playIndexPosition != -1) {
            notifyItemChanged(playIndexPosition);
        }
        if (audioInfo == null) {
            playIndexPosition = -1;
            playIndexHash = "-1";
            return;
        }
        //;
        playIndexPosition = getPlayIndexPosition(audioInfo);
        if (playIndexPosition != -1) {
            playIndexHash = audioInfo.getHash();
            notifyItemChanged(playIndexPosition);
        }

    }

    /**
     * 获取当前播放索引
     *
     * @param audioInfo
     * @return
     */
    public int getPlayIndexPosition(AudioInfo audioInfo) {
        if (audioInfo != null)
            for (int i = 0; i < mDatas.size(); i++) {

                if (mDatas.get(i).getHash().equals(audioInfo.getHash())) {

                    return i;
                }
            }

        return -1;
    }


    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
    }

    public void setState(int state) {
        this.state = state;
    }

    /////////////////////////////////////////////////////

    class PopListViewHolder extends RecyclerView.ViewHolder {
        private View view;
        /**
         * item底部布局
         */
        private ListItemRelativeLayout listItemRelativeLayout;

        /**
         * 歌手头像按钮
         */
        private CircleImageView singPicImg;
        /**
         * 歌曲索引
         */
        private TextView songIndexTv;

        /**
         * 歌曲名称
         */
        private TextView songNameTv;

        /**
         * 歌手名称
         */
        private TextView singerNameTv;
        /**
         * 是否存在本地
         */
        private ImageView islocalImg;
        /**
         * 下载未完成按钮
         */
        private ImageView downloadImg;
        /**
         * 添加喜欢按钮
         */
        private IconfontTextView unlikeTv;

        /**
         * 下载完成按钮
         */
        private ImageView downloadedImg;

        /**
         * 喜欢按钮
         */
        private ImageView likeImg;

        public PopListViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public ListItemRelativeLayout getListItemRelativeLayout() {
            if (listItemRelativeLayout == null) {
                listItemRelativeLayout = view.findViewById(R.id.itemBG);
            }
            return listItemRelativeLayout;
        }


        public TextView getSongNameTv() {
            if (songNameTv == null) {
                songNameTv = view.findViewById(R.id.songName);
            }
            return songNameTv;
        }

        public TextView getSingerNameTv() {
            if (singerNameTv == null) {
                singerNameTv = view.findViewById(R.id.singerName);
            }
            return singerNameTv;
        }

        public ImageView getIslocalImg() {
            if (islocalImg == null) {
                islocalImg = view.findViewById(R.id.islocal);
            }
            return islocalImg;
        }

        public CircleImageView getSingPicImg() {
            if (singPicImg == null) {
                singPicImg = view.findViewById(R.id.singPic);
            }
            return singPicImg;
        }

        public TextView getSongIndexTv() {
            if (songIndexTv == null) {
                songIndexTv = view.findViewById(R.id.songIndex);
            }
            return songIndexTv;
        }

        public ImageView getDownloadImg() {
            if (downloadImg == null) {
                downloadImg = view.findViewById(R.id.download);
            }
            return downloadImg;
        }

        public IconfontTextView getUnLikeTv() {
            if (unlikeTv == null) {
                unlikeTv = view.findViewById(R.id.unlike);
            }
            return unlikeTv;
        }

        public ImageView getDownloadedImg() {
            if (downloadedImg == null) {
                downloadedImg = view.findViewById(R.id.downloaded);
            }
            return downloadedImg;
        }

        public ImageView getLikedImg() {
            if (likeImg == null) {
                likeImg = view.findViewById(R.id.liked);
            }
            return likeImg;
        }
    }

}
