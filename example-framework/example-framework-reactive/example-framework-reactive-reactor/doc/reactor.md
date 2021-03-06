[TOC]

## 1 调度类型

- Schedulers.elastic()：线程池中的线程是可以复用的，按需创建与空闲回收，该调度器适用于I/O密集型任务。
- Schedulers.parallel()：含有固定个数的线程池，该调度器适用于计算密集型任务，其中的线程数量取决于 CPU 的核的数量。
- Schedulers.single()：单一线程来执行任务。
- Schedulers.immediate()：立刻使用调用线程来执行。
- Schedulers.fromExecutor()：将已有的Executor转换为Scheduler来执行任务。

## 2 数据信号

数据信号：元素值、错误信号、完成信号，错误信号和完成信号都是终止信号，不能同事共存。完成信号用于告知下游订阅者该数据流正常结束，错误信号终止数据流的同时将错误传递给下游订阅者。