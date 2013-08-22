package com.example.lifememory.activity.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.EditText;


public class LinedEditText extends EditText {
     private Paint linePaint;
     private float margin;
     private int paperColor;

     public LinedEditText(Context paramContext, AttributeSet paramAttributeSet) {
         super(paramContext, paramAttributeSet);
         this.linePaint = new Paint();
         this.linePaint.setColor(Color.BLUE);
     }

     protected void onDraw(Canvas paramCanvas) {
         paramCanvas.drawColor(this.paperColor);
         int i = getLineCount();
         int j = getHeight();
         int k = getLineHeight();
         int m = 1 + j / k;
         if (i < m)
             i = m;
         int n = getCompoundPaddingTop();
         paramCanvas.drawLine(0.0F, n, getRight(), n, this.linePaint);
         for (int i2 = 0;; i2++) {
             if (i2 >= i) {
                 setPadding(10 + (int) this.margin, 0, 0, 0);
                 super.onDraw(paramCanvas);
                 paramCanvas.restore();
                 return;
             }
             n += k;
             paramCanvas.drawLine(0.0F, n, getRight(), n, this.linePaint);
             paramCanvas.save();
         }
     }
     
 	@Override
 	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
 		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
 		super.onMeasure(widthMeasureSpec, expandSpec);
 	}
}
	
