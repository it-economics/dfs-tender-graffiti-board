import React, {PropsWithChildren} from 'react';
import './App.css';

function App() {
	return (
		<div className="min-h-screen bg-gray-50 dark:bg-gray-800 text-gray-900 dark:text-gray-50 md:pt-5 font-body">
			<div className="md:container md:mx-auto">
				<h1 className="sm:text-3xl md:text-5xl text-center lg:pt-10 lg:pb-10">Graffiti AG</h1>
				<Graffities/>
			</div>
		</div>
	);
}

interface IGraffiti {
	message: string
	author?: string
}

function useGraffities(): IGraffiti[] {
	return [
		{message: "What's up?!"},
		{message: 'London Calling', author: 'Paul'},
		{message: 'Yellow Submarine', author: 'Ringo'},
		{message: 'Lemon Tree', author: 'John'},
		{message: 'Those are not the droids you are looking for', author: 'Obi-Wan'},
	];
}

function Graffities() {
	const graffities = useGraffities();

	return (
		<div className="grid sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 md:gap-10 sm:gap-7">
			{graffities.map((graffiti, idx) =>
				<Graffiti key={`${graffiti.message.substr(0, 5)}-${graffiti.author}-${idx}`} graffiti={graffiti}/>
			)}
		</div>
	);
}

function Graffiti({graffiti: {message, author}}: PropsWithChildren<{ graffiti: IGraffiti }>) {
	function color(): string {
		const rand = Math.random();
		let color: string;
		if (rand < 0.25) {
			color = 'yellow';
		} else if (rand < 0.5) {
			color = 'blue';
		} else if (rand < 0.75) {
			color = 'red';
		} else {
			color = 'green';
		}
		return `dark:text-${color}-500 text-${color}-800`;
	}

	function rotate(): string {
		if (Math.random() < 0.5) {
			return 'rotate-6'
		}
		return '-rotate-6'
	}

	return (
		<div className={`transform w-full grid items-end ${color()} `}>
			<span className={`transform sm:text-1xl md:text-3xl ${rotate()}`}>{message}</span>
			<span className="text-right mx-28 my-10">{`- ${author ?? 'Anonymous'}`}</span>
		</div>
	)
}

export default App;
