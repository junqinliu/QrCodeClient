package com.android.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.android.qrcodeclient.R;


/**
 * Created by liujunqin on 2016/8/11.
 */
public class VoiceUtil {

    public static VoiceUtil voiceUtil;
    private SoundPool sp;//声明一个SoundPool
    private int music;//定义一个整型用load（）；来设置suondID


    public VoiceUtil(Context context) {

        sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(context, R.raw.erweima, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级

    }

    public static VoiceUtil getInstance(Context context) {
        if (voiceUtil == null)
            voiceUtil = new VoiceUtil(context);
        return voiceUtil;
    }

   public void startVoice(){

       sp.play(music, 1, 1, 0, 0, 1);


   }

}
