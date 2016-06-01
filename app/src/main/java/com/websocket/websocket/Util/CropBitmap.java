package com.websocket.websocket.Util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

/**
 *   이미지를 원으로 자르는 클래스
 */
public class CropBitmap {
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();

        // View의 사이즈 만큼 사각형을 만든다. (left, right, top, bottom)
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true); // paint의 경계면을 부드럽게 처리
        canvas.drawARGB(0, 0, 0, 0); // 투명도와 RGB값 설정 ( A = 투명도 )
        paint.setColor(color);

        // 주어진 두 점을 중심으로 반지름만큼 paint로 원을 그림(x, y, 반지름, paint)
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);

        // 원본이미지와 canvas 합성 ( SRC_IN : 원본이미지와 canvas로 그린 원과 겹치는 부분만 그려짐 )
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // bitmap을 주어진 좌표를 좌측 상단에 맞추어 paint로 그림 (bitmap, left, top, paint)
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
