'use client';

import { HeroUIProvider } from '@heroui/react';

export function Provider({ children }) {
	return <HeroUIProvider>{children}</HeroUIProvider>;
}
