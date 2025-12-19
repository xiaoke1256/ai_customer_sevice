'use client';

import {TestButton} from './test_button'

// export const metadata = {
// 	title: 'ai客服系统',
// };

export default function Page() {
	return (
		<div className='h-screen p-4 flex flex-col gap-2'>
			<div >测试主页面</div>
			<a href="/chat" >转向聊天页面</a>
			<TestButton/>
		</div>
	);
}
