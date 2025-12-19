'use client';

import {ChevronLeftIcon} from '@heroicons/react/24/outline';
import {Button,Input} from '@heroui/react'

export function Chat() {
    //从后台获取上次聊天的sessionId
    //从后台获取上次聊天的session内容
    //若从后台无法获取sessionId，就生成一个新的SessionId

    const sendMsg = (e)=>{
        const source = new EventSource('/openSseChat',
            {
                headers: {
                    'Authorization': 'Bearer your_token',
                    'Custom-Header': 'custom_value'
                }
            }
        );
        source.onmessage = function(event) { // 当接收到消息时触发此函数
            console.log('New message:', event.data); // 打印接收到的数据
        };
        source.onopen = function(event){
            console.log('onopen:', event);
        }
        source.onerror = function(event){

        }
        source.onclose = function(event){

        }
    }
    
    
    return (
        <div className="h-full mt-5 mb-5 max-w-[960px] w-full bg-blue-200 bg-opacity-75 flex flex-col justify-between">
            <div className="head m-2">
                <a href="/"><ChevronLeftIcon className='h-6'/></a>
            </div>
            <div className="content flex-1">

            </div>
            <div className="foot m-2 flex">
                <Input className="m-1"></Input>
                <Button color='primary' className="m-1" onPress={sendMsg}>发送</Button>
            </div>

        </div>
    )
}