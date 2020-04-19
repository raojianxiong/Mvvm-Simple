# Mvvm-Simple

> AppArchitecture 一个加深理解MVVM的架构设计的项目

## 目录

* Android中常见的开发模式
* MVVM的M、VM、V的创建流程
* 本项目MVVM介绍
* RxJava操作符
* MVVM日常操作
* MVVM数据绑定分析



## Android中常见的开发模式

MVC

> View：XML布局
>
> Model：实体模型(数据获取、村粗、数据状态变化)
>
> Controller：对应于Activity、处理数据、业务和UI

MVP

> View：对应于Activity和XML，负责View的绘制以及与用户的交互
>
> Model：对应于实体模型
>
> Presenter：负责完成View和Model间的交互和业务逻辑

MVVM

> View：对应于Activity和XML，负责View的绘制以及用户交互
>
> Model：实体模型
>
> ViewModel：负责完成View与Model间的交互，负责业务逻辑

## MVVM的M、VM、V的创建流程

>  V -> VM -> M

## 本项目MVVM介绍

1. 基本模块

   主模块和其它模块通过ARouter实现接口通信

   ![](https://github.com/raojianxiong/Mvvm-Simple/blob/master/AppArchitecture/pictures/1.jpg)

   ![](https://github.com/raojianxiong/Mvvm-Simple/blob/master/AppArchitecture/pictures/2.jpg)

   ![](https://github.com/raojianxiong/Mvvm-Simple/blob/master/AppArchitecture/pictures/6.PNG)

2. 遇见错误不要慌，有空Clear和Rebuild

   1. DataBinding问题定位对于新手来说，有点难，代码难维护，因为数据往往是双向绑定，你根据id不好找到对应的调用代码
   2. 查看日志 ./gradlew build --stacktrace 可以查看详细日志
   3. 检查xml中@{}是不是写错了，类型是否正确，因为xml没有检测这个是否对错的机制
   4. @BindingAdapter对于系统属性会覆盖，是不是覆盖了系统属性
   5. 如果出现DataBindingImpl不能转换成DataBinding类相关的错误，clear and rebuild
   6. 多模块依赖，使用到DataBinding的模块请配置databinding { enable = true}

## RXJava操作符

> RxJava在Android中常用操作，可参考其他人的总结
>
> https://www.jianshu.com/p/f5c3e7b88005
>
> https://www.jianshu.com/p/25682d620320

![](https://github.com/raojianxiong/Mvvm-Simple/blob/master/AppArchitecture/pictures/3.jpg)

## MVVM日常操作

![](https://github.com/raojianxiong/Mvvm-Simple/blob/master/AppArchitecture/pictures/4.jpg)

## MVVM数据绑定分析

1. 在MvvmActivity中实现了Observer接口，在onChanged中写了一些有关网络请求成功或异常的显示逻辑，原因是因为MutableLiveData<ViewStatus>会触发该onChanged方法，在基类中有写LiveData.setObserve(this,this)方法，这里将Observer对象传递给了LiveData

   ![](https://github.com/raojianxiong/Mvvm-Simple/blob/master/AppArchitecture/pictures/7.jpg)

2. 网络请求数据绑定

   1. 在上述分析的onChange中，我们拿到数据后，接受到改变后，会触发adapter的刷新，adapter刷新等同于我们写的viewmodl要刷新，举个例子，就是TitleView需要刷新，代码里通过getDataBinding().setViewModel(data)更新了viewmodl,更新了xml中的数据。你可能会问，为什么就刷新了数据 ？ 如下
   2. DataBindingUtil类通过setContentView或者inflate方法，将布局放入DataBinderMapper中缓存在sMapper中， 在我们setViewModel时会调用notifyPropertyChanged(int fieldId)，fieldId传入的时BR.viewModel，最终会在sMapper中拿到该值进行更新。

#### 彩蛋

##### 快乐时刻

![](https://github.com/raojianxiong/Mvvm-Simple/blob/master/AppArchitecture/pictures/5.jpg)







































