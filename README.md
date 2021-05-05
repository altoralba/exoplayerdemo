# exoplayerdemo
An ExoPlayer written in Java (Android). it currently supports on/off volume control and fixed 16:9 aspect ratio.

## What is ExoPlayer
>ExoPlayer is an open source project that is not part of the Android framework and is distributed separately from the Android SDK. ExoPlayer’s standard audio and video components are built on Android’s MediaCodec API, which was released in Android 4.1 (API level 16). Because ExoPlayer is a library, you can easily take advantage of new features as they become available by updating your app.
>ExoPlayer supports features like Dynamic adaptive streaming over HTTP (DASH), SmoothStreaming and Common Encryption, which are not supported by MediaPlayer. It's designed to be easy to customize and extend.

More info about ExoPlayer can be found [here](https://developer.android.com/guide/topics/media/exoplayer).

## Usage
If you are planning to use a different source, you can change the string from the following:
```
MediaItem mediaItem = new MediaItem.Builder()
                .setUri(getString(R.string.media_url_hls))
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .build();
```
Alternatively, you can modify the string resource found in the values folder to add your own sources of media.
