import React from 'react'

function endpointConfigurator(endpointBase: string) {
    return process.env.REACT_APP_NGINX == 'true' ? endpointBase : process.env.REACT_APP_BASE_URL + endpointBase;
}

export default endpointConfigurator