'use client';

import { useState, useEffect } from 'react';
import {
	Input,
	Button,
	Radio,
	RadioGroup,
	Table,
	TableHeader,
	TableColumn,
	TableBody,
	TableRow,
	TableCell,
	getKeyValue,
	Pagination,
} from '@heroui/react';
import { queryLog } from '../sdk/sdk.js';

const columns = [
	{
		key: 'index',
		label: '序号',
	},
	{
		key: 'timestamp',
		label: '时间',
	},
	{
		key: 'errorCode',
		label: '错误码',
	},
	{
		key: 'log',
		label: '日志内容',
	},
];

const NUMBER_PER_PAGE = 1000;

export function QueryComponent() {
	const [sn, setSn] = useState('');
	const [userId, setUserId] = useState('');
	const [logType, setLogType] = useState('device');
	const [logs, setLogs] = useState({});
	const [disabled, setDisabled] = useState(false);

	const logsViewData =
		logs?.logs?.map(({ timestamp, ...props }, i) => ({
			index: i + 1,
			timestamp: new Date(timestamp).toLocaleString(),
			...props,
		})) || [];

	async function queryPage(page) {
		try {
			setDisabled(true);
			console.log(sn);
			console.log(userId);
			if (logType === 'device' && !sn) return;
			if (logType === 'app' && !userId) return;
			const result = await queryLog(
				'',
				logType,
				sn,
				userId,
				NUMBER_PER_PAGE,
				page
			);
			if (result.result === 'failure') return;
			setLogs(result);
			console.log(result);
		} finally {
			setDisabled(false);
		}
	}

	useEffect(() => {
		const [tableWrapper] = document.getElementsByClassName('main-scroll-div');
		tableWrapper.scrollIntoView(false);
	}, [logs]);

	return (
		<>
			<div className='flex flex-row gap-2'>
				<RadioGroup
					orientation='horizontal'
					defaultValue={logType}
					onValueChange={setLogType}
				>
					<Radio value='device'>设备日志</Radio>
					<Radio value='app'>App日志</Radio>
				</RadioGroup>
				{logType === 'device' && (
					<Input
						isDisabled={disabled}
						placeholder='请输入皮可宝设备的SN'
						value={sn}
						onValueChange={setSn}
					/>
				)}
				{logType === 'app' && (
					<Input
						isDisabled={disabled}
						placeholder='请输入用户Id，或 “noLogin”'
						value={userId}
						onValueChange={setUserId}
					/>
				)}
				<Button isDisabled={disabled} onPress={() => queryPage(0)}>
					查询
				</Button>
			</div>
			<Table
				isStriped
				removeWrapper
				isHeaderSticky
				aria-label='logs'
				classNames={{ base: 'flex-1 overflow-auto', table: 'main-scroll-div' }}
			>
				<TableHeader columns={columns}>
					{(column) => (
						<TableColumn key={column.key}>{column.label}</TableColumn>
					)}
				</TableHeader>
				<TableBody items={logsViewData}>
					{(item) => (
						<TableRow key={item._id}>
							{(columnKey) => (
								<TableCell className='whitespace-pre-line'>
									{getKeyValue(item, columnKey)}
								</TableCell>
							)}
						</TableRow>
					)}
				</TableBody>
			</Table>
			<Pagination
				showControls
				isDisabled={disabled}
				total={Math.ceil((logs?.count || 0) / NUMBER_PER_PAGE) || 1}
				page={logs?.currentPage || 1}
				onChange={(page) => queryPage(page)}
			/>
		</>
	);
}
