'use client';

import {ChevronLeftIcon} from '@heroicons/react/24/outline';
import {Button,Input,Avatar} from '@heroui/react';
import { useState, useEffect,useCallback } from 'react';
import { v4 as uuid } from 'uuid';

export function Chat() {
    //从后台获取上次聊天的sessionId
    //从后台获取上次聊天的session内容
    //若从后台无法获取sessionId，就生成一个新的sessionId
    const [msgs,setMsgs] = useState([]);

    const [sessionId,setSessionId] = useState('');

    useEffect(()=>{
        setSessionId(uuid());
        window.addEventListener('resize',handleResize)
        handleResize()

    },[]);

    const handleResize = () => {
        console.log('Window was resized!');
        const chartContent = document.getElementById('chartContent');
        const pre = chartContent.previousSibling;
        const next = chartContent.nextSibling;
        const chartParent = document.getElementById('chartFull');
        var viewportHeight = window.innerHeight;
        console.log("chartParent.style.marginTop:",chartParent.style.marginTop.replace("px",""));
        console.log("chartParent.style.marginBottom:",chartParent.style.marginBottom.replace("px",""));
        const newHeight = viewportHeight- pre.offsetHeight-next.offsetHeight-chartParent.style.marginTop.replace("px","")-chartParent.style.marginBottom.replace("px","");
        console.log("newHeight:"+newHeight)
        chartContent.style.height=newHeight+'px';
    };

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
        setMsgs([...msgs,{from:'user',content:userPrompt}]);
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
            if (event.type === 'message'){
                setMsgs((currentMsg)=>{
                    const last = currentMsg[currentMsg.length-1];
                    const rest = [...currentMsg.slice(0, currentMsg.length-1)];
                    if(last.from=='ai'){
                       return [...rest,{from:'ai',content:last.content+event.data}];
                    }else{
                       return [...currentMsg,{from:'ai',content:event.data}];
                    }
                });
            } 
            
        };
        source.onopen = function(event) {
            console.log('onopen:', event);
        }
        source.onerror = function(event){
            console.log('onerror:', JSON.stringify(event));
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
        <div id="chartFull" className="h-full max-w-[960px] w-full bg-blue-200 bg-opacity-75 flex flex-col justify-between" style={{marginTop:20,marginBottom:20}} >
            <div className="head p-2">
                <a href="/"><ChevronLeftIcon className='h-6'/></a>
            </div>
            <div id="chartContent" className="content overflow-y-scroll">
                {
                    msgs.map((msg,index)=>{
                        console.log("msg.from:",msg.from);
                        if(msg.from==='ai'){//ai客户或人工客服,靠左显示
                            return (<div key={index} className='flex justify-start my-2' >
                                <Avatar color="default" radius="lg" size="lg" showFallback src="agent.svg" className="ml-2" />
                                <div
                                    style={{ 
                                        marginTop:'10px',
                                        backgroundImage: 'url(delta.svg)',
                                        backgroundSize: "cover",
                                        backgroundPosition: "center",
                                        width:"12px",
                                        height:"12px"
                                    }} 
                                ></div>
                                <div className="max-w-[80%] p-2 bg-[#D1F4E0]/[0.6] rounded" style={{ whiteSpace: 'pre-wrap' }}>{msg.content}</div>
                            </div>);
                        }else{
                            return (<div key={index} className='flex justify-end my-2' >
                                <div className="max-w-[80%] p-2 bg-[#D1F4E0]/[0.6] rounded" style={{ whiteSpace: 'pre-wrap' }} >{msg.content}</div>
                                <div
                                    style={{ 
                                        marginTop:'10px',
                                        backgroundImage: 'url(delta.svg)',
                                        backgroundSize: "cover",
                                        backgroundPosition: "center",
                                        transform: "scaleX(-1)",
                                        width:"12px",
                                        height:"12px"
                                    }} 
                                ></div>
                                <Avatar color="default" radius="lg" size="lg" showFallback src="user.svg" className="mr-2" />
                            </div>);
                        }
                    })
                }
            </div>
            <div className="foot p-2 flex">
                <Input className="m-1" value={userPrompt} onValueChange={setUserPrompt} ></Input>
                <Button color='primary' className="m-1" onPress={sendMsg}>发送</Button>
            </div>

        </div>
    )
}