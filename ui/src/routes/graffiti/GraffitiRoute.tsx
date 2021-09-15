import React, {PropsWithChildren} from "react";
import {GraffitiContextProvider, IGraffiti, useGraffitiContext} from "./GraffitiContext";

export function GraffitiRoute() {
	return (
		<>
			<h1 className="text-3xl md:text-5xl text-center py-10">Graffiti AG</h1>
			<GraffitiContextProvider>
				<div className="flex flex-col max-h-full">
					<div className="pb-5 md:pb-16">
						<GraffitiForm/>
					</div>
					<div>
						<Graffities/>
					</div>
				</div>
			</GraffitiContextProvider>
		</>
	);
}

function Graffities() {
	const {graffities} = useGraffitiContext();
	return (
		<div className="flex flex-row flex-wrap gap-3 md:gap-5 justify-center">
			{[...graffities].reverse().map((graffiti, idx) =>
				<Graffiti key={`${graffiti.message.substr(0, 5)}-${graffiti.author}-${idx}`} graffiti={graffiti}/>
			)}
		</div>
	);
}

function Graffiti({graffiti: {message, author}}: PropsWithChildren<{ graffiti: IGraffiti }>) {
	function color(): string {
		const rand = Math.random();
		if (rand < 0.25) {
			return `dark:text-yellow-500 text-yellow-800`;
		} else if (rand < 0.5) {
			return `dark:text-blue-500 text-blue-800`;
		} else if (rand < 0.75) {
			return `dark:text-red-500 text-red-800`;
		}
		return `dark:text-green-500 text-green-800`;
	}

	function rotate(): string {
		if (Math.random() < 0.5) {
			return 'rotate-6'
		}
		return '-rotate-6'
	}

	return (
		<div className={`transform grid items-end ${color()} `}>
			<span className={`transform sm:text-1xl md:text-3xl ${rotate()}`}>{message}</span>
			<span className="text-right mx-28 my-3 md:my-5">{`- ${author ?? 'Anonymous'}`}</span>
		</div>
	)
}

function GraffitiForm() {
	const {addGraffiti} = useGraffitiContext();
	const [message, setMessage] = React.useState<string>('')
	const [author, setAuthor] = React.useState<string>('')
	const canSubmit = message.length > 0;

	const handleSubmit: React.FormEventHandler<HTMLFormElement> = event => {
		event.preventDefault();
		if (canSubmit) {
			const submitAuthor = author !== '' ? author : undefined
			addGraffiti({message, author: submitAuthor})
			setMessage('')
			setAuthor('')
		}
	}

	return (
		<form onSubmit={handleSubmit}>
			<div className="flex justify-center">
				<div className="flex flex-col items-center w-full md:w-auto md:items-end md:flex-row gap-5">
					<div>
						<label className="block">
							<span>Nachricht</span>
							<input type="text" maxLength={300} minLength={1} value={message} onChange={e => setMessage(e.target.value)}
								   className="rounded mt-2 block dark:bg-gray-900 border-gray-600 focus:border-green-700 focus:ring-green-700"
								   id="message"/>
						</label>
					</div>
					<div>
						<label className="block">
							<span>Autor</span>
							<input type="text" maxLength={50} value={author} onChange={e => setAuthor(e.target.value)}
								   className="rounded mt-2 block dark:bg-gray-900 border-gray-600 focus:border-green-700 focus:ring-green-700"
								   id="author"/>
						</label>
					</div>
					<div className="md:flex md:flex-column md:items-end">
						<button disabled={!canSubmit} type="submit" className="rounded dark:bg-green-900 dark:hover:bg-green-800 p-3">Senden</button>
					</div>
				</div>
			</div>
		</form>
	);
}
