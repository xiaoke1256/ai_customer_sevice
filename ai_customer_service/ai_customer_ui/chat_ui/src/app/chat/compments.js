'use client';

import {ChevronLeftIcon} from '@heroicons/react/24/outline';
import {Button,Input} from '@heroui/react';
import {useState } from 'react';
import { v4 as uuid } from 'uuid';

export function Chat() {
    //从后台获取上次聊天的sessionId
    //从后台获取上次聊天的session内容
    //若从后台无法获取sessionId，就生成一个新的sessionId
    const sessionId = uuid();

    console.log("sessionId:",sessionId);

    const [userPrompt,setUserPrompt] = useState('');

    const sendMsg = (e)=>{
        console.log('sendMsg ... ');
        const source = new EventSource(`http://localhost:8080/openSseChat?userPrompt=${userPrompt}`,
            {
                headers: {
                    'sessionId': sessionId
                }
            }
        );
        source.addEventListener('start', (event) => {
            console.log('Custom event:', event.data);
        });
        source.addEventListener('complete', (event) => {
            console.log('Custom event:', event.data);
            //source.close();
            //console.log('调用了close' );
        });
        source.onmessage = function(event) { // 当接收到消息时触发此函数
            console.log('New message:', event.type,event.data); // 打印接收到的数据
        };
        source.onopen = function(event){
            console.log('onopen:', event);
        }
        source.onerror = function(event){
            console.log('onerror:', event);
            console.log('event.target.readyState:', event.target.readyState);
            if (event.target.readyState == EventSource.CLOSED) {
                console.log('Connection closed');
                if (event.target.lastEventId) {
                    console.log('Last event ID:', event.target.lastEventId);
                }
                if (event.target.url) {
                    console.log('SSE URL:', event.target.url);
                }
            } else {
                console.log('EventSource error:', event);
                source.close();
            }
            
        }
        source.onclose = function(event){
            console.log('onclose:', event);
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
                <Input className="m-1" value={userPrompt} onValueChange={setUserPrompt} ></Input>
                <Button color='primary' className="m-1" onPress={sendMsg}>发送</Button>
            </div>

        </div>
    )
}