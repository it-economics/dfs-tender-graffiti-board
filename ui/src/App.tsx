import React from 'react';
import './App.css';
import {GraffitiRoute} from "./routes/graffiti/GraffitiRoute";

function App() {
	return (
		<div className="min-h-screen h-full bg-gray-50 dark:bg-gray-800 text-gray-900 dark:text-gray-50 py-5 font-body text-sm md:text-xl">
			<div className="md:container md:mx-auto">
				<GraffitiRoute/>
			</div>
		</div>
	);
}

export default App;
