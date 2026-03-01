package jdk9;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;


public class ReactiveStreamExample {
    public static void main(String[] args) {
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();

        // 创建一个订阅者
        Subscriber<Integer> subscriber = new Subscriber<>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                this.subscription.request(1); // 请求一个元素
            }

            @Override
            public void onNext(Integer item) {
                System.out.println("Received: " + item);
                subscription.request(1); // 请求下一个元素
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done");
            }
        };

        // 订阅发布者
        publisher.subscribe(subscriber);
        // 发送数据
        publisher.submit(1);
        publisher.submit(2);
        publisher.submit(3);


        try {
            Thread.sleep(1000);//主线程太快结束，导致onNext函数未来得及处理
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        publisher.close(); // 完成发送数据
    }
}
