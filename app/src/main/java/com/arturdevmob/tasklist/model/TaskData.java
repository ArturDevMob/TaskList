package com.arturdevmob.tasklist.model;

public class TaskData {
    private long mId;
    private String mTitle;
    private String mDescription;
    private Priority mPriority;
    private long mCreationDate;
    private long mDeadline;
    private boolean mDone;

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority mPriority) {
        this.mPriority = mPriority;
    }

    public long getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(long mCreationDate) {
        this.mCreationDate = mCreationDate;
    }

    public long getDeadline() {
        return mDeadline;
    }

    public void setDeadline(long deadline) {
        mDeadline = deadline;
    }

    public boolean getDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }

    public static class Priority {
        private int mId;
        private int mTitleRes;
        private int mColorRes;

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            this.mId = id;
        }

        public int getTitleRes() {
            return mTitleRes;
        }

        public void setTitleRes(int title) {
            this.mTitleRes = title;
        }

        public int getColorRes() {
            return mColorRes;
        }

        public void setColorRes(int color) {
            this.mColorRes = color;
        }
    }
}