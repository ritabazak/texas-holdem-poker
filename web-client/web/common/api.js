const ERRORS = {
    'USERNAME_ALREADY_TAKEN': 'The name you have chosen is already in use!',
    'DUPLICATE_GAME_TITLE': 'Game title already in use!',
    'INVALID_HANDS_COUNT': 'There must be enough hands for all players to play an equal amount of hands!',
    'INVALID_BLINDS': 'Big blind must be larger than the small blind!',
    'GAME_ALREADY_IN_PROGRESS': 'Game already in progress!',
    'GAME_FULL': 'Game full!',
    'UNSUPPORTED_GAME_TYPE': 'Unsupported game type!'
};

class Http {
    static get(url, queryParams) {
        return new Promise((resolve, reject) => {
            if (queryParams) {
                url += '?' + Object.keys(queryParams)
                    .map(key => key + '=' + encodeURIComponent(queryParams[key]))
                    .join('&');
            }

            fetch(url, { credentials: 'same-origin' })
                .then(httpRes => httpRes.json())
                .then(res => {
                    if (res.success) {
                        resolve(res.data);
                    }
                    else {
                        throw new Error(res.error);
                    }
                })
                .catch(err => {
                    toastr.error(ERRORS[err.message]);
                    reject(err);
                });
        });
    }

    static post(url, data, queryParams) {
        return new Promise((resolve, reject) => {
            if (queryParams) {
                url += '?' + Object.keys(queryParams)
                    .map(key => key + '=' + encodeURIComponent(queryParams[key]))
                    .join('&');
            }

            fetch(url, {
                method: 'POST',
                body: JSON.stringify(data),
                credentials: 'same-origin',
                headers: new Headers({
                    'Content-Type': 'application/json'
                })
            })
                .then(httpRes => httpRes.json())
                .then(res => {
                    if (res.success) {
                        resolve(res.data);
                    }
                    else {
                        throw new Error(res.error);
                    }
                })
                .catch(err => {
                    toastr.error(ERRORS[err.message]);
                    reject(err);
                });
        });
    }
}

class Url {
    static getParams() {
        const params = {};

        let i = window.location.href.indexOf('?');

        if (i > -1) {
            let encodedParams = window.location.href.substring(i + 1).split('&');
            encodedParams.forEach(paramStr => {
                let key = paramStr.split('=')[0];
                let val = paramStr.split('=')[1];

                params[key] = decodeURIComponent(val);
            });
        }

        return params;
    }
}