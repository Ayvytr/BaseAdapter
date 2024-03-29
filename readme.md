# BaseAdapter

基于张鸿洋的BaseAdapter，最新改版后只支持androidx。废弃了Wrapper包装Adapter的做法，增加了`EmptyAdapter`，
可继承实现空布局效果。可配合专业的下拉刷新库实现下拉刷新、空布局显示。



## 引入

```
mavenCentral()
implementation 'io.github.ayvytr:base-adapter:0.1.4'
```



## 使用

继承CommonAdapter，`onBindView()` 初始化View，可使用继承支持空布局的EmptyAdapter，可在`onBindEmptyView()`实现空布局点击事件。



例子：

```java
public class DataEmptyAdapter extends EmptyAdapter<Bean> {
    public DataEmptyAdapter(@NotNull Context context, int layoutId, int emptyLayoutId) {
        super(context, layoutId, emptyLayoutId);
    }

    @Override
    protected void onBindView(ViewHolder holder, Bean bean, int position, List payloads) {
        holder.setText(R.id.tv, bean.text);
    }


    @Override
    public void onBindEmptyView(@NotNull ViewHolder holder) {
		//可实现空布局点击事件
    }
}
```





## 更新历史
* 0.1.4
  * 修改ItemViewDelegateManager.addDelegate()

* 0.1.3
  * 修改ViewHolder.getView()返回值不可空

* 0.1.2
  * 修改增加CallSuper后EmptyAdapter CI lint报错问题
* ~~0.1.1~~
  * 删除setOnItemClickListener(), setOnItemLongClickListener()
  * 修改部分变量名称和访问权限
  * MultiItemTypeAdapter.clear() 取消空判断，防止本来列表是空，EmptyAdapter.clear()之后空布局不显示问题
* 0.1.0
  * 适配androidx
  * 废弃了Wrapper包装Adapter的做法，增加了`EmptyAdapter`，可继承实现空布局效果



## 参考/感谢：

[https://github.com/hongyangAndroid/baseAdapter](https://github.com/hongyangAndroid/baseAdapter)

