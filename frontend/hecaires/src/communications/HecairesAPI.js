export default class HecairesAPI {
  static instace;

  static API_BASE_URL = 'http://localhost:8080';

  static endpoints = {
    api: {
      auth: {
        signin: {
          resource: '/api/auth/signin',
          method: 'POST',
          accepts: 'json',
          returns: 'json'
        },
        signup: {},
        verify: {
          resource: '/api/auth/verify',
          method: 'GET',
          returns: 'json'
        }
      },
      test: {
        all: {
          resource: '/api/test/all',
          method: 'GET'
        },
        user: {},
        maintainer: {},
        admin: {}
      }
    }
  };

  /**
   * Fetch API headers
   */
  headers = {};

  constructor() {
    if (HecairesAPI.instace !== undefined) {
      throw new Error('HecairesAPI has a singleton pattern and must refer to its own instance via the static method HecairesAPI.getInstance()!');
    }
  }

  /**
   * @returns {HecairesAPI}
   */
  static getInstance() {
    if (HecairesAPI.instace === undefined) {
      HecairesAPI.instace = new HecairesAPI();
    }

    return HecairesAPI.instace;
  }

  /**
   * @param {{
   *  name: string,
   *  value: string
   * }} header 
   */
  addHeader(header) {
    this.headers[header.name] = header.value;
  }

  /**
   * 
   * @param {{
   *  resource: string,
   *  method: string,
   *  accepts: 'json' | 'string',
   *  returns: 'json' | 'string'
   * }} endPoint 
   * @param {object} payload
   * 
   * @returns {
   *  responsePromise: Promise,
   *  abortController: AbortController
   * }
   */
  request(endPoint, payload) {
    console.log(endPoint, payload);
    const abortController = new AbortController();
    const options = {
      method: endPoint.method,
      signal: abortController.signal,
      headers: {
        ...this.headers
      },
      body: null
    };

    if (payload) {
      if (endPoint.accepts === 'json') {
        options.headers['Content-Type'] = 'application/json';
      }

      options.body = JSON.stringify(payload);
    }

    let responsePromise = fetch(
      HecairesAPI.API_BASE_URL + endPoint.resource,
      options
    );

    if (endPoint.returns === 'json') {
      responsePromise = responsePromise.then((response) => response.json());
    }

    return {
      responsePromise,
      abortController
    };
  }
}