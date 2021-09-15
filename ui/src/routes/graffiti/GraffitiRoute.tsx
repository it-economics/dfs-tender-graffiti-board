import React, {PropsWithChildren} from "react";

export function GraffitiRoute() {
	return (
		<>
			<h1 className="sm:text-3xl md:text-5xl text-center lg:pt-10 lg:pb-10">Graffiti AG</h1>
			<Graffities/>
			<GraffitiForm/>
		</>
	);
}

interface IGraffiti {
	message: string
	author?: string
}

interface IGraffities {
	graffities: IGraffiti[]
}

function useGraffities(): IGraffities {
	const staticGraffities = [
		{message: "What's up?!"},
		{message: 'London Calling', author: 'Paul'},
		{message: 'Yellow Submarine', author: 'Ringo'},
		{message: 'Lemon Tree', author: 'John'},
		{message: 'Those are not the droids you are looking for', author: 'Obi-Wan'},
	];
	return {
		graffities: staticGraffities
	};
}

function Graffities() {
	const {graffities} = useGraffities();

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

function GraffitiForm() {
	return (
		<form onSubmit={(e) => {
			e.preventDefault()
		}}>
			<div className="flex justify-center">
				<div className="flex gap-5">
					<div>
						<label className="block">
							<span>Nachricht</span>
							<input type="text" maxLength={300}
								   className="rounded mt-2 block dark:bg-gray-900 border-gray-600 focus:border-green-700 focus:ring-green-700"
								   id="message"/>
						</label>
					</div>
					<div>
						<label className="block">
							<span>Autor</span>
							<input type="text"
								   className="rounded mt-2 block dark:bg-gray-900 border-gray-600 focus:border-green-700 focus:ring-green-700"
								   id="author"/>
						</label>
					</div>
					<div className="flex flex-column items-end">
						<button type="submit" className="rounded dark:bg-green-900 dark:hover:bg-green-800 p-3">Senden</button>
					</div>
				</div>
			</div>
		</form>
	);
}