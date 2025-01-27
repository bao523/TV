package com.fongmi.android.tv.player.exo;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.media3.common.util.Log;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector;
import androidx.media3.exoplayer.video.VideoRendererEventListener;

import com.fongmi.android.tv.player.Players;

import java.util.ArrayList;

import io.github.anilbeesetti.nextlib.media3ext.ffdecoder.FfmpegVideoRenderer;

public class NextRenderersFactory extends DefaultRenderersFactory {

    private static final String TAG = NextRenderersFactory.class.getSimpleName();
    private final int decode;

    public NextRenderersFactory(@NonNull Context context, int decode) {
        super(context);
        this.decode = decode;
        setEnableDecoderFallback(true);
        setExtensionRendererMode(Players.isHard(decode) ? EXTENSION_RENDERER_MODE_ON : EXTENSION_RENDERER_MODE_PREFER);
    }

    @Override
    protected void buildVideoRenderers(@NonNull Context context, int extensionRendererMode, @NonNull MediaCodecSelector mediaCodecSelector, boolean enableDecoderFallback, @NonNull Handler eventHandler, @NonNull VideoRendererEventListener eventListener, long allowedVideoJoiningTimeMs, @NonNull ArrayList<Renderer> out) {
        super.buildVideoRenderers(context, extensionRendererMode, mediaCodecSelector, enableDecoderFallback, eventHandler, eventListener, allowedVideoJoiningTimeMs, out);
        if (Players.isHard(decode)) return;
        int extensionRendererIndex = out.size();
        try {
            Renderer renderer = new FfmpegVideoRenderer(allowedVideoJoiningTimeMs, eventHandler, eventListener, MAX_DROPPED_VIDEO_FRAME_COUNT_TO_NOTIFY);
            out.add(extensionRendererIndex++, renderer);
            Log.i(TAG, "Loaded FfmpegVideoRenderer.");
        } catch (Exception e) {
            throw new RuntimeException("Error instantiating Ffmpeg extension", e);
        }
    }
}
