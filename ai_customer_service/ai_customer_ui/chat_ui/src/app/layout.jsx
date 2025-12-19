import { Provider } from './provider';
import './main.css';

// export const dynamic = 'force-static';
// export const revalidate = 0;
// export const fetchCache = 'default-no-store';

export default function RootLayout({ children }) {
	return (
		<html lang='en'>
			<body>
				<Provider>{children}</Provider>
			</body>
		</html>
	);
}
