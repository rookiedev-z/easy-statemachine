# easy-statemachine

首先声明这个项目是基于 [stateless4j](https://github.com/stateless4j/stateless4j) 项目改造而来，并且出于学习的目的根据个人想法同时参考 [squirrel](https://github.com/hekailiang/squirrel) 来进行优化，于是便有了现在这个项目。

## Introduction

在介绍这个项目之前，先看下 [维基百科](https://zh.wikipedia.org/wiki/%E6%9C%89%E9%99%90%E7%8A%B6%E6%80%81%E6%9C%BA) 上面关于有限状态机的描述。

有限状态机（英语：finite-state machine，缩写：FSM）又称有限状态自动机（英语：finite-state automation，缩写：FSA），简称状态机，是表示有限个状态以及在这些状态之间的转移和动作等行为的数学计算模型。

状态存储关于过去的信息，就是说：它反映从系统开始到现在时刻的输入变化，转移指示状态变更，并且用必须满足确使转移发生的条件来描述它，动作是在给定时刻要进行的活动的描述，有多种类型的动作：

- 进入动作（entry action）：在进入状态时进行
- 退出动作（exit action）：在退出状态时进行
- 输入动作：依赖于当前状态和输入条件进行
- 转移动作：在进行特定转移时进行

FSM（有限状态机）可以使用状态转移图或者状态转移表来表示，下面展示了最常见的表示：

当前状态（B）和条件（Y）的组合指示出下一个状态（C），完整的动作信息可以只使用脚注来增加，包括完整动作信息的 FSM 定义可以使用状态表。

状态转移表:

| **条件 ↓ \ 当前状态 →** | 状态 A | 状态 B | 状态 C |
| :---------------------: | :----: | :----: | :----: |
|         条件 X          |   …    |   …    |   …    |
|         条件 Y          |   …    | 状态 C |   …    |
|         条件 Z          |   …    |   …    |   …    |

* 状态总数（state）是有限的。
* 任一时刻，只处在一种状态之中。
* 某种条件下，会从一种状态转变（transition）到另一种状态。

看完上面的描述你应该大概能猜到这个项目是干嘛的了，没错了，就是对有限状态机轻量简单的实现，在 [stateless4j](https://github.com/stateless4j/stateless4j) 的基础上加上了自己的理解进行改造，目前还在继续探索中，如果你对代码实现有质疑的地方或者有其他更好的想法，欢迎提交 Issue 或者 PR 进行交流。

## Maven

```xml
<dependency>
  <groupId>net.gittab.statemachine</groupId>
  <artifactId>easy-statemachine</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Quick Start

创建一个 Maven 项目并正确包含 easy-statemachine 依赖项，然后在 main 方法中添加并且运行以下代码:

```java
StateMachineBuilder.Builder<OrderState, OrderEvent, OrderContext> builder =  StateMachineBuilder.builder();

builder.externalConfigure(OrderState.TO_PAY).permit(OrderEvent.PAY_SUCCESS, OrderState.PAID, (transition, context) ->{
            logger.info(transition.getSource() + " -> " + transition.getDestination() +  " on " + transition.getEvent() +  " event with " + context);
        });

builder.externalConfigure(OrderState.TO_PAY).permit(OrderEvent.PAY_FAIL, OrderState.PAID_FAILED, (transition, context) ->{
  logger.info(transition.getSource() + " -> " + transition.getDestination() +  " on " + transition.getEvent() +  " event with " + context);
});

builder.externalConfigure(OrderState.PAID).permit(OrderEvent.REFUND_SUCCESS, OrderState.REFUNDED, (transition, context) ->{
  logger.info(transition.getSource() + " -> " + transition.getDestination() +  " on " + transition.getEvent() +  " event with " + context);
});

builder.internalConfigure(OrderState.PAID).permit(OrderEvent.REFUND_FAIL, (transition, context) ->{
  logger.info(transition.getSource() + " -> " + transition.getDestination() +  " on " + transition.getEvent() +  " event with " + context);
});

StateMachine<OrderState, OrderEvent, OrderContext> stateMachine = builder.newStateMachine(OrderState.TO_PAY);

stateMachine.fire(OrderEvent.PAY_SUCCESS);

stateMachine.fire(OrderEvent.REFUND_FAIL);

assertEquals(OrderState.PAID, stateMachine.getState());
```

上面代码可以在当前项目测试代码中 [StateMachineBuilderTest](https://github.com/rookiedev-z/easy-statemachine/blob/master/src/test/java/net/gittab/statemachine/StateMachineBuilderTest.java) 找到。

## User Guide

TODO

## Future Plan

TODO

## More Information

- 接下来我会在微信公众号: **rookiedev** 上面详细介绍该项目，敬请关注
- 对于讨论或问题，可以添加我的微信: **rookie-dev**
- 如有任何问题或者要求，请提交 [Issue](https://github.com/rookiedev-z/easy-statemachine/issues)

## License

[Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html)

## WeChat Public Account

扫描下方二维码关注我的微信公众号 **rookiedev** 实时查看最新分享的文章和干货

![rookiedev](rookiedev.jpg)
