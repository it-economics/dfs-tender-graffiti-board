import * as React from "react";
import {PropsWithChildren} from "react";
import {webSocket} from "rxjs/webSocket";

export interface IGraffiti {
	message: string
	author?: string
}

export interface IGraffitiContext {
	graffities: IGraffiti[]
	addGraffiti: (graffiti: IGraffiti) => void
}

const GraffitiContext = React.createContext<IGraffitiContext>({
	graffities: [],
	addGraffiti: () => {
	}
});

const subject = webSocket<IGraffiti>("ws://localhost:8085/message-socket");

export function GraffitiContextProvider({children}: PropsWithChildren<{}>) {
	const [dynamicGraffities, setDynamicGraffities] = React.useState<IGraffiti[]>([])
	const handleNewGraffiti = (graffiti: IGraffiti) => {
		subject.next(graffiti)
	}
	React.useEffect(() => {
		subject
			.subscribe({
				next: (msg) => setDynamicGraffities(old => [...old, msg]),
				error: (error) => console.error(error),
				complete: () => console.info("Unsubscribed")
			})
		return () => {
			subject.unsubscribe()
		}
	}, [])
	return (
		<GraffitiContext.Provider value={{
			graffities: dynamicGraffities,
			addGraffiti: handleNewGraffiti
		}}>
			{children}
		</GraffitiContext.Provider>
	);
}

export const useGraffitiContext = () => React.useContext(GraffitiContext)
