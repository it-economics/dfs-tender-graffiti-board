import * as React from "react";
import {PropsWithChildren} from "react";

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

const staticGraffities = [
	{message: "What's up?!"},
	{message: 'London Calling', author: 'Paul'},
	{message: 'Yellow Submarine', author: 'Ringo'},
	{message: 'Lemon Tree', author: 'John'},
	{message: 'Those are not the droids you are looking for', author: 'Obi-Wan'},
];

export function GraffitiContextProvider({children}: PropsWithChildren<{}>) {
	const [dynamicGraffities, setDynamicGraffities] = React.useState<IGraffiti[]>([])
	return (
		<GraffitiContext.Provider value={{
			graffities: [...staticGraffities, ...dynamicGraffities],
			addGraffiti: graffiti => setDynamicGraffities(old => [...old, graffiti])
		}}>
			{children}
		</GraffitiContext.Provider>
	);
}

export const useGraffitiContext = () => React.useContext(GraffitiContext)
