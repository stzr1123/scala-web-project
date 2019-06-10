import React from 'react';

class SunWeatherComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            sunrise: null,
            sunset: null,
            temperature: null,
            requests: null
        };
    }
    render = () => {
        return <div>
            <div>Sunrise time: {this.state.sunrise}</div>
            <div>Sunset time: {this.state.sunset}</div>
            <div>Current temperature: {this.state.temperature}</div>
            <div>Requests: {this.state.requests}</div>
        </div>
    }
}

export default SunWeatherComponent;
