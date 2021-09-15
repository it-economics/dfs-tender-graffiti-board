import React from 'react';
import './App.css';
import {GraffitiRoute} from "./routes/graffiti/GraffitiRoute";

function App() {
	return (
		<div className="min-h-screen bg-gray-50 dark:bg-gray-800 text-gray-900 dark:text-gray-50 md:pt-5 font-body">
			<div className="md:container md:mx-auto">
				<GraffitiRoute/>
			</div>
		</div>
	);
}

export default App;
