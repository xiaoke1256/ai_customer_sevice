'use client';

import { Chat} from './compments'

export default function ChatIndex() {
    return (
    <>
        <div className="flex h-screen flex-col items-center justify-around" 
            style={{ 
                backgroundImage: 'url(chat_bg.png)',
                backgroundSize: "cover",
                backgroundPosition: "center" 
            }} 
        >
           <Chat/>
        </div>
    </>
    );
}