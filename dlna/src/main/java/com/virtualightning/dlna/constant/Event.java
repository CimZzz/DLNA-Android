package com.virtualightning.dlna.constant;

public interface Event {
    int DEFAULT = -1;

    int STATUS_OK = 0;
    int STATUS_OCCUR_ERROR = 1;

    int STATE_PLAYING = 0;
    int STATE_STOPPED = 1;
    int STATE_PAUSED_PLAYBACK = 2;
    int STATE_TRANSPORTING = 3;
    int STATE_NO_MEDIA_PRESENT = 4;
}
