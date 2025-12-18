'use client';

import { HeroUIProvider } from '@heroui/react';

export function Provider({ children,...rest }) {
	return <HeroUIProvider
		{...rest}
	>{children}</HeroUIProvider>;
}
