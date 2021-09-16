import {findWords, isPalindrome} from "./palindrome";

describe('palindrome', () => {
	it.each([
		['Otto', true],
		['OttO', true],
		['Karl', false],
		['Ot to', true],
	])('is it one?', (input, shouldBePalindrome) => {
		expect(isPalindrome(input)).toEqual(shouldBePalindrome)
	});

	it.each([
		['Hello World,ABC', ['Hello', 'World', 'ABC']],
		['Hello      World', ['Hello', 'World']],
		['      ', []],
		['xy-123-bb tat', ['xy', '123', 'bb', 'tat']],
		['Otto OttoKarl', ['Otto', 'OttoKarl']],
	])('find all words', (input, expectedOutput) => {
		const matches = /\b\w+\b/ig.exec('Hello World')
		expect(matches).not.toBeNull()

		const value = 'Hello World';
		expect(value.replace(/\b\w+\b/gi, it => isPalindrome(it) ? 'Foo' : 'Bar')).toEqual('(Hello) (World)')

		if (matches !== null)
			expect(matches.index).toBe(0)
		expect(findWords(input)).toEqual(expectedOutput)
	});
})
