package com.cuthead.controller;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by Jiaqi Ning on 21/3/2015.
 */
public class AnimationUtil {
    public static void showRevealEffect (final View revealView, int centerX, int centerY, SupportAnimator.AnimatorListener lis) {

        revealView.setVisibility(View.VISIBLE);

        // get the final radius for the clipping circle
        int finalRadius = Math.max(revealView.getWidth(), revealView.getHeight());
        SupportAnimator animator =
                ViewAnimationUtils.createCircularReveal(revealView, centerX, centerY, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(350);
        animator.addListener(lis);
        animator.start();
    }

}
