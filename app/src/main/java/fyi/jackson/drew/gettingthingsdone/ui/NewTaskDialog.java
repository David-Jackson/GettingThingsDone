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
import android.text.Editable;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;

import fyi.jackson.drew.gettingthingsdone.R;
import fyi.jackson.drew.gettingthingsdone.animation.EndAnimatorListener;

public class NewTaskDialog {

    public static final int MODE_NEW_TASK = 872;
    public static final int MODE_NEW_BUCKET = 418;

    private boolean dialogVisible = false;
    private int mode = MODE_NEW_TASK;
    private CardView dialogNewTask;
    private View dialogScrim;
    private EditText etTaskInput;

    public FloatingActionButton fab;
    private AnimatedVectorDrawable avdAddToDone, avdDoneToAdd;

    public NewTaskDialog(Activity activity) {
        dialogNewTask = activity.findViewById(R.id.dialog_new_task);
        dialogScrim = activity.findViewById(R.id.new_task_scrim);
        etTaskInput = activity.findViewById(R.id.et_task_input);
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
        if (!dialogVisible) setModeTask();
    }

    public void showDialog() {
        if (!dialogVisible) showHideDialog();
    }

    public void hideDialog() {
        if (dialogVisible) showHideDialog();
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
        CoordinatorLayout.LayoutParams dialogLayoutParams =
                (CoordinatorLayout.LayoutParams) dialogNewTask.getLayoutParams();

        dialogLayoutParams.gravity = dialogVisible ? Gravity.CENTER : Gravity.BOTTOM;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AutoTransition transition = new AutoTransition();
            transition.setOrdering(TransitionSet.ORDERING_TOGETHER);
            TransitionManager.beginDelayedTransition((ViewGroup) dialogNewTask.getRootView(), transition);
        }

        dialogNewTask.setLayoutParams(dialogLayoutParams);
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

    public boolean isVisible() {
        return this.dialogVisible;
    }

    public int getMode() {
        return this.mode;
    }

    public void setModeTask() {
        this.mode = this.MODE_NEW_TASK;
        setHint(R.string.dialog_hint_new_task);
    }

    public void setModeBucket() {
        this.mode = this.MODE_NEW_BUCKET;
        setHint(R.string.dialog_hint_new_bucket);
    }

    public Editable getText() {
        return this.etTaskInput.getText();
    }

    public void setText(CharSequence text) {
        this.etTaskInput.setText(text);
    }

    public void setHint(CharSequence hint) {
        this.etTaskInput.setHint(hint);
    }

    public void setHint(int redId) {
        this.etTaskInput.setHint(redId);
    }

    public boolean requestFocus() {
        return this.etTaskInput.requestFocus();
    }

    public void clearFocus() {
        this.etTaskInput.clearFocus();
    }
}
