'use client';

import { Image,Link } from '@heroui/react';
import "./index.css";

// export const metadata = {
// 	title: 'ai客服系统',
// };

export default function Page() {
	return (
		<div className='h-screen p-4 flex flex-col gap-2'>
			<div >测试主页面</div>
			<a href="/chat" >转向聊天页面</a>
			<div className="fixed-div">
				<Link href='/chat'>
					<Image width={64} src="agent.svg" ></Image>
				</Link>
			</div>
		</div>
	);
}
