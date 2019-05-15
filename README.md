> 在工作中难免遇到自定义 `View` 的相关需求，本身这方面比较薄弱，因此做个记录，也是自己学习和成长的积累。[自定义View实战](https://link.jianshu.com/?t=http%3A%2F%2Fxiaweizi.cn%2Fcategories%2F%25E8%2587%25AA%25E5%25AE%259A%25E4%25B9%2589View%25E5%25AE%259E%25E6%2588%2598%2F).

## 前言

这个版本主要的任务就是完成 **环信客服**系统的集成，上一篇文章 [仿IOS下载View](http://xiaweizi.cn/article/5d77/) 也是这个版本开发需求中的一小部分，那今天介绍一下另一个小需求 **客服好评**。 **客服好评** 的功能在于用户对客服服务态度和质量的评价，也是作为考核客服服务的标准。相关代码已上传 [EvaluationCardView](https://github.com/xiaweizi/EvaluationCardView)

<!-- more -->

看一下预览效果：

![整体预览](http://xiaweizi.top/2018-03-31-%E6%95%B4%E4%BD%93%E9%A2%84%E8%A7%88.gif)

> 需求简要说明
>
> 1. 默认状态为0星，不可提交
> 2. 星星数量小于等于3，展示差评理由
> 3. 差评理由云控，数量可变
> 4. 差评理由可不选，可多选

我将分为3部分进行介绍 。

![介绍](http://xiaweizi.top/2018-03-31-image-201803311622551.png)

1. 评级的 `RatingBar`
2. 差评理由 `TagView`
3. 整体评价的 `CardView`

## EvaluationRatingBar

#### 介绍

`Android` 原生就有这个空间 `RatingBar`,定制型不是很高，所以需要通过自定义来满足特定的产品需求。其实 `RatingBar`的主要用处就在于 **评级**，基本就是对服务进行等级评价，来决定服务的质量如何。

#### 需求分析

有需求才会有对应的实现，那么有哪些需要控制的属性呢。

| 属性名称       | 属性介绍           |
| -------------- | ------------------ |
| mStarTotal     | 评级的总数         |
| mSelectedCount | 评级选中的数量     |
| mStarResId     | 星星的资源文件     |
| mHeight        | 星星的高度         |
| mIntervalWidth | 星星之间间隔的宽度 |
| mEditable      | 是否可被点击       |

#### 具体实现

既然星星有两种状态可供选择，那么单个 `View` 就使用 `CheckBox` 代替，首先初始化的时候，需要根据 `mStarTotal` 来控制添加多少个 `CheckBox` ，并根据 `mHeight` 高度和 `mIntervalWidth` 间隔来控制摆放的位置。

```java
for (int i = 0; i < mStarTotal; i++) {
    CheckBox cb = new CheckBox(getContext());
    LayoutParams layoutParams;
    if (mHeight == 0) {
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    } else {
        layoutParams = new LayoutParams((int) mHeight, (int) mHeight);
    }

    layoutParams.gravity = Gravity.CENTER_VERTICAL;
    if (i != 0 && i != mStarTotal - 1) {
        layoutParams.leftMargin = (int) mIntervalWidth;
        layoutParams.rightMargin = (int) mIntervalWidth;
    } else if (i == 0) {
        layoutParams.rightMargin = (int) mIntervalWidth;
    } else if (i == mStarTotal - 1) {
        layoutParams.leftMargin = (int) mIntervalWidth;
    }
    addView(cb, layoutParams);
}
```

最后在父布局 `LinearLayout` 中添加 所有的 `CheckBox`。

至于点击事件的回调，可以在每次点击的时候进行遍历，获取 `CheckBox ` 的选中状态，并通过 `callback` 回调出来。

```java
for (int i = 0; i < mStarTotal; i++) {
    CheckBox cb = (CheckBox) getChildAt(i);
    if (i <= position) {
    	cb.setChecked(true);
    } else if (i > position) {
    	cb.setChecked(false);
    }
}
if (mOnRatingChangeListener != null) {
    mOnRatingChangeListener.onChange(mSelectedCount);
}
```

最后的效果：

![EvaluationRatingBar](http://xiaweizi.top/2018-03-31-ratingBar.gif)

## EvaluationNegReasonsLayout

#### 需求分析

当用户给出差评的时候，需要展示对应的差评理由选择。理由云控，数量可变，内容可变。可单选，可不选，可多选。

主要的难点和重点在于根据理由内容的长短进行展示，如果内容长则显示一条，如果内容短可以显示多条。

#### 具体实现

我们都知道 `View` 的测量工作主要是在 `onMeasure` 里进行。 **宽度计算**，可以先测量出每个子 `View` 的宽度，每次叠加，如果超过父布局限制的宽度则换行。 **高度计算**，每次换行叠加高度，每一行的高度取子 `View` 高度的最大值。

```java
//遍历每个子元素
for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
    View childView = getChildAt(i);
    //测量每一个子view的宽和高
    measureChild(childView, widthMeasureSpec, heightMeasureSpec);

    //获取到测量的宽和高
    int childWidth = childView.getMeasuredWidth();
    int childHeight = childView.getMeasuredHeight();

    //因为子View可能设置margin，这里要加上margin的距离
    MarginLayoutParams mlp = (MarginLayoutParams) childView.getLayoutParams();
    int realChildWidth = childWidth + mlp.leftMargin + mlp.rightMargin;
    int realChildHeight = childHeight + mlp.topMargin + mlp.bottomMargin;

    //如果当前一行的宽度加上要加入的子view的宽度大于父容器给的宽度，就换行
    if ((lineWidth + realChildWidth) > sizeWidth) {
    //换行
    resultWidth = Math.max(lineWidth, realChildWidth);
    resultHeight += realChildHeight;
    //换行了，lineWidth和lineHeight重新算
    lineWidth = realChildWidth;
    lineHeight = realChildHeight;
    } else {
    //不换行，直接相加
    lineWidth += realChildWidth;
    //每一行的高度取二者最大值
    lineHeight = Math.max(lineHeight, realChildHeight);
    }

    //遍历到最后一个的时候，肯定走的是不换行
    if (i == childCount - 1) {
    resultWidth = Math.max(lineWidth, resultWidth);
    resultHeight += lineHeight;
    }
}
setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : resultWidth,modeHeight == MeasureSpec.EXACTLY ? sizeHeight : resultHeight);
```

既然 **宽高** 计算完了，剩下就是子 `View` 的摆放了，自然是在在 `onLayout()` 中实现。摆放就比较简单了，同样需要遍历所有的子 `View`，最终调用 `layout(left, top, right, bottom)` 方法进行位置的摆放。宽度不断叠加，当超过父布局的宽度，则将 `left` 置为 0，高度记上一行子 `View` 的最大高度，以此类推。

```java
@Override
protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int realWidht = getWidth();
    int childLeft = 0;
    int childTop = 0;

    //遍历子控件，记录每个子view的位置
    for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
        View childView = getChildAt(i);

        //跳过View.GONE的子View
        if (childView.getVisibility() == View.GONE) {
            continue;
        }

        //获取到测量的宽和高
        int childWidth = childView.getMeasuredWidth();
        int childHeight = childView.getMeasuredHeight();

        //因为子View可能设置margin，这里要加上margin的距离
        MarginLayoutParams mlp = (MarginLayoutParams) childView.getLayoutParams();

        if (childLeft + mlp.leftMargin + childWidth + mlp.rightMargin > realWidht) {
            //换行处理
            childTop += (mlp.topMargin + childHeight + mlp.bottomMargin);
            childLeft = 0;
        }
        //布局
        int left = childLeft + mlp.leftMargin;
        int top = childTop + mlp.topMargin;
        int right = childLeft + mlp.leftMargin + childWidth;
        int bottom = childTop + mlp.topMargin + childHeight;
        childView.layout(left, top, right, bottom);
        childLeft += (mlp.leftMargin + childWidth + mlp.rightMargin);
    }
}
```

来看一下最终的效果：

![reasonsLayout](http://xiaweizi.top/2018-03-31-ReasonLayout.gif)

## EvaluationCardView

这个就简单了，配合着 `AlertDialog` 弹窗显示，将之前介绍的 `EvaluationRatingBar` 和 `EvaluationNegReasonsLayout` 结合在一块，并根据自己特殊的产品需求来定制对应的效果。最后在点击提交的时候通过接口回调的方式，将最终的结果回调出来并处理。

```java
public void setOnEvaluationCallback(OnEvaluationCallback callback) {
    this.mCallback = callback;
}

public interface OnEvaluationCallback {
    void onEvaluationCommitClick(int starCount, Set<String> reasons);
}
```

> starCount: 即为评级的等级。reasons：即为选择的差评理由

最终调用

```java
EvaluationCardView cardView = new EvaluationCardView(this);
List<String> reasonsData = new ArrayList<>();
reasonsData.add("回复太慢");
reasonsData.add("对业务不了解");
reasonsData.add("服务态度差");
reasonsData.add("问题没有得到解决");
cardView.setReasonsData(reasonsData);
cardView.show();
cardView.setOnEvaluationCallback(new EvaluationCardView.OnEvaluationCallback() {
    @Override
    public void onEvaluationCommitClick(int starCount, Set<String> reasons) {
        StringBuilder sb = new StringBuilder();
        for (String reason : reasons) {
            sb.append("\n").append(reason);
        }
        Toasty.success(EvaluationCardViewActivity.this, "评价成功\n" + "星星数量：" + starCount + "\n差评理由：" + sb.toString(), Toast.LENGTH_LONG, true).show();
    }
});
```

具体的实现代码请查看 [EvaluationCardView](https://github.com/xiaweizi/EvaluationCardView)。

## 感谢

[FlowTag](https://github.com/hanhailong/FlowTag)

[原文地址](http://xiaweizi.cn/article/18a/)
