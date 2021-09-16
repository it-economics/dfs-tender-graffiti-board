export function isPalindrome(input: string): boolean {
	const lowerInput = input.toLowerCase();
	return lowerInput === lowerInput.split('').reverse().join('');
}

export function findWords(input: string): string[] {
	// return [...input.matchAll(/\b[a-zA-Z]+\b/gi)].flatMap(x => x)
	// @ts-ignore
	return [...input.matchAll(/\b\w+\b/gi)].flatMap(x => x)
}
