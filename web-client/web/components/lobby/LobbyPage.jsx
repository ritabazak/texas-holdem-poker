class LobbyPage extends React.Component {
    intervalID = null;

    constructor(props) {
        super(props);

        this.state = {
            players: [],
            games: [],
            username: ''
        };

        this.refreshLobbyInfo = this.refreshLobbyInfo.bind(this);
        this.onGameAdd = this.onGameAdd.bind(this);
        this.joinGame = this.joinGame.bind(this);
    }

    componentDidMount() {
        this.refreshLobbyInfo();
        this.intervalID = setInterval(this.refreshLobbyInfo, 1000);
    }

    componentWillUnmount() {
        clearInterval(this.intervalID);
    }

    refreshLobbyInfo() {
        Http.get('api/lobby')
            .then(this.setState.bind(this));
    }

    onGameAdd(xmlContent) {
        Http.post('api/lobby', { xmlContent: xmlContent })
            .then(this.refreshLobbyInfo);
    }

    logout() {
        Http.post('api/logout')
            .finally(() => window.location.href = 'index.html');
    }

    joinGame(gameId) {
        Http.post('api/room', null, {id: gameId, method: 'JOIN'})
            .then(() => window.location.href = 'room.html?id=' + encodeURIComponent(gameId))
    }


    render() {
        return (
            <div className="page flex-column">
                <header>
                    <h1 className="text-center">Game Lobby</h1>
                    <div>Hello {this.state.username}!</div>
                    <button className="btn" onClick={this.logout}>Logout...</button>
                </header>

                <div className="page-body flex-row flex">
                    <div className="left-sidebar">
                        <PlayerList players={this.state.players} />
                    </div>
                    <div className="main-content flex">
                        <div className="container">
                            <GameList games={this.state.games} onJoin={this.joinGame} onAdd={this.onGameAdd} />
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}