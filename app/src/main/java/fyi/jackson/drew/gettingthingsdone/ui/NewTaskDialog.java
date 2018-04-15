package fyi.jackson.drew.gettingthingsdone.ui;

import android.animation.Animator;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import fyi.jackson.drew.gettingthingsdone.R;
import fyi.jackson.drew.gettingthingsdone.animation.EndAnimatorListener;

public class NewTaskDialog {

    private boolean dialogVisible = false;
    private CardView dialogNewTask;
    private View dialogScrim;

    private FloatingActionButton fab;
    private AnimatedVectorDrawable avdAddToDone, avdDoneToAdd;

    public NewTaskDialog(Activity activity) {
        dialogNewTask = activity.findViewById(R.id.dialog_new_task);
        dialogScrim = activity.findViewById(R.id.new_task_scrim);
        fab = activity.findViewById(R.id.fab);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            avdAddToDone = (AnimatedVectorDrawable)
                    activity.getResources().getDrawable(R.drawable.avd_add_to_done);
            avdDoneToAdd = (AnimatedVectorDrawable)
                    activity.getResources().getDrawable(R.drawable.avd_done_to_add);
        }

        dialogScrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideDialog();
            }
        });
    }

    public void showHideDialog() {
        dialogVisible = !dialogVisible;

        // Order here is important:
        // updateDialogLayoutParams invokes TransitionManager.beginDelayedTransition
        // which makes the dialog fade in and out if called before circularRevealDialog
        circularRevealDialog();
        updateDialogLayoutParams();
        updateScrim();
        updateFabIcon();
    }

    private void circularRevealDialog() {
        int revealStartX = dialogNewTask.getWidth();
        int revealStartY = dialogNewTask.getHeight();
        float dialogHypot = (float) Math.hypot(dialogNewTask.getHeight(), dialogNewTask.getWidth());
        float revealStartRadius, revealEndRadius;

        if (dialogVisible) {
            revealStartRadius = 0;
            revealEndRadius = dialogHypot;
        } else {
            revealStartRadius = dialogHypot;
            revealEndRadius = 0;
        }

        if (dialogVisible) dialogNewTask.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    dialogNewTask, revealStartX, revealStartY, revealStartRadius, revealEndRadius);
            if (!dialogVisible) {
                circularReveal.addListener(new EndAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dialogNewTask.setVisibility(View.INVISIBLE);
                    }
                });
            }
            circularReveal.setInterpolator(new AccelerateDecelerateInterpolator());
            circularReveal.start();
        } else {
            if (!dialogVisible) dialogNewTask.setVisibility(View.INVISIBLE);
        }
    }

    private void updateDialogLayoutParams() {
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) dialogNewTask.getLayoutParams();

        params.gravity = dialogVisible ? Gravity.CENTER : Gravity.BOTTOM;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AutoTransition transition = new AutoTransition();
            transition.setOrdering(TransitionSet.ORDERING_TOGETHER);
            TransitionManager.beginDelayedTransition((ViewGroup) dialogNewTask.getRootView(), transition);
        }

        dialogNewTask.setLayoutParams(params);
    }

    private void updateScrim() {
        dialogScrim.setVisibility(dialogVisible ? View.VISIBLE : View.INVISIBLE);
    }

    // Updates the icon from add to done with Animated Vector Drawable if possible
    private void updateFabIcon() {
        // Run animated vector drawable if >= Lollipop, if not, just change the drawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (dialogVisible) {
                fab.setImageDrawable(avdAddToDone);
                avdAddToDone.start();
            } else {
                fab.setImageDrawable(avdDoneToAdd);
                avdDoneToAdd.start();
            }
        } else {
            fab.setImageResource(
                    dialogVisible ? R.drawable.ic_done_black_24dp : R.drawable.ic_add_black_24dp);
        }
    }
}
