'use client';

import { Image,Link,Card } from '@heroui/react';
import "./index.css";

// export const metadata = {
// 	title: 'ai客服系统',
// };

export default function Page() {
	return (
		<div className='h-screen p-4 flex flex-col gap-2'>
			<div >这里是AI客服系统</div>
			<div >请点击右下角图标，转向<a href="/chat" >客服聊天页面</a></div>
			<Card className="fixed-div mr-6 mb-6">
				<Link href='/chat'>
					<Image width={64} src="agent.svg" ></Image>
				</Link>
			</Card>
		</div>
	);
}
