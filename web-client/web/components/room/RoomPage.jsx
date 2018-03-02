class RoomPage extends React.Component {
    intervalID = null;
    betModal;
    raiseModal;
    winnersModal;
    endGameModal;

    constructor(props) {
        super(props);

        this.state = {
            game: null,
            hand: null,
            betAmount: 1,
            raiseAmount: 1
        };

        this.refreshRoomInfo = this.refreshRoomInfo.bind(this);
        this.toggleReady = this.toggleReady.bind(this);
        this.buyIn = this.buyIn.bind(this);
        this.fold = this.fold.bind(this);
        this.check = this.check.bind(this);
        this.call = this.call.bind(this);
        this.bet = this.bet.bind(this);
        this.raise = this.raise.bind(this);
        this.leaveGame = this.leaveGame.bind(this);
    }

    componentDidMount() {
        this.refreshRoomInfo();
        this.intervalID = setInterval(this.refreshRoomInfo, 1000);
    }

    componentWillUnmount() {
        clearInterval(this.intervalID);
    }

    componentDidUpdate(prevProps, prevState) {
        if (prevState.hand) {
            if (!prevState.hand.winners.length && this.state.hand.winners.length) {
                this.winnersModal.show();
            }
        }

        if (prevState.game) {
            if (prevState.game.gameOn && !this.state.game.gameOn) {
                this.endGameModal.show();
            }
        }
    }

    refreshRoomInfo() {
        Http.get('api/room?id=' + encodeURIComponent(Url.getParams().id))
            .then(this.setState.bind(this));
    }

    get selfGamePlayer() {
        let {game, username} = this.state;
        return game.players.find(player => player.name === username);
    }

    isCurrentPlayer() {
        let {hand, username} = this.state;
        let currentPlayer = hand.players.find(player => player.current);
        return currentPlayer? currentPlayer.name === username : false;
    }

    get gameId() {
        return Url.getParams().id;
    }

    toggleReady() {
        Http.post('api/room', null, {
            id: this.gameId,
            method: this.selfGamePlayer.ready? 'UNREADY': 'READY'
        })
            .then(this.refreshRoomInfo);
    }

    leaveGame() {
        console.log(this.gameId)
        Http.post('api/room', null, {
            id: this.gameId,
            method: 'LEAVE'
        })
            .then(() => window.location.href = 'lobby.html')
    }

    buyIn() {
        Http.post('api/room', null, {
            id: this.gameId,
            method: 'BUYIN'
        })
            .then(() => {
                this.refreshRoomInfo();
                toastr.success('$$$ Successfully bought-in! $$$')
            });
    }

    fold() {
        Http.post('api/room', null, {
            id: this.gameId,
            method: 'FOLD'
        })
            .then(this.refreshRoomInfo);
    }

    check() {
        Http.post('api/room', null, {
            id: this.gameId,
            method: 'CHECK'
        })
            .then(this.refreshRoomInfo);
    }

    call() {
        Http.post('api/room', null, {
            id: this.gameId,
            method: 'CALL'
        })
            .then(this.refreshRoomInfo);
    }

    bet(amount) {
        Http.post('api/room', {amount}, {
            id: this.gameId,
            method: 'BET'
        })
            .then(this.refreshRoomInfo);
    }

    raise(amount) {
        Http.post('api/room', {amount}, {
            id: this.gameId,
            method: 'RAISE'
        })
            .then(this.refreshRoomInfo);
    }

    renderBottomMenu() {
        let {game, hand} = this.state;

        if (game.gameOn && game.handInProgress) {
            let notCurrentPlayer = !this.isCurrentPlayer()
            let disableFold = notCurrentPlayer;
            let disableCheck = notCurrentPlayer || hand.betActive;
            let disableCall = notCurrentPlayer || !hand.betActive;
            let disableBet = notCurrentPlayer || hand.betActive || hand.maxBet <= 0;
            let disableRaise = notCurrentPlayer || !hand.betActive || hand.maxBet <= 0;

            return (
                <footer className="flex-row">
                    <button className="flex btn btn-secondary" onClick={this.fold} disabled={disableFold}>Fold</button>
                    <button className="flex btn btn-secondary" onClick={this.check} disabled={disableCheck}>Check</button>
                    <button className="flex btn btn-secondary" onClick={this.call} disabled={disableCall}>Call</button>
                    <button className="flex btn btn-secondary" onClick={() => this.betModal.show()} disabled={disableBet}>Bet</button>
                    <button className="flex btn btn-secondary" onClick={() => this.raiseModal.show()} disabled={disableRaise}>Raise</button>
                </footer>
            );
        }

        let disableReady = !game.gameOn;
        let disableBuyIn = this.selfGamePlayer.type === 'COMPUTER';
        let disableLeaveGame = this.selfGamePlayer.type === 'COMPUTER' && !game.joinable;

        return (
            <footer className="flex-row">
                <button className="flex btn btn-secondary" onClick={this.toggleReady} disabled={disableReady}>
                    {!this.selfGamePlayer.ready && 'Ready'}
                    {this.selfGamePlayer.ready && 'Unready'}
                </button>
                <button className="flex btn btn-secondary" onClick={this.buyIn} disabled={disableBuyIn}>Buy-In</button>
                <button className="flex btn btn-danger" onClick={this.leaveGame} disabled={disableLeaveGame}>Leave Game</button>
            </footer>
        );
    }

    render() {
        let {game, hand} = this.state;

        if (!game) {
            return <div>Loading...</div>
        }

        return (
            <div className="page flex-column">
                {hand && (
                    <Modal title="Bet Amount" onOK={() => this.bet(this.state.betAmount)} ref={m => this.betModal = m}>
                        <div>
                            <input type="range"
                                   min="1"
                                   max={hand.maxBet}
                                   value={this.state.betAmount}
                                   onChange={e => this.setState({betAmount: e.target.value})}
                                   style={{width: '100%'}} />
                        </div>
                        <div className="text-center">
                            <i className="fab fa-bitcoin"></i>
                            &nbsp;
                            {this.state.betAmount}
                        </div>
                    </Modal>
                )}

                {hand && (
                    <Modal title="Raise Amount" onOK={() => this.raise(this.state.raiseAmount)} ref={m => this.raiseModal = m}>
                        <div>
                            <input type="range"
                                   min="1"
                                   max={hand.maxBet}
                                   value={this.state.raiseAmount}
                                   onChange={e => this.setState({raiseAmount: e.target.value})}
                                   style={{width: '100%'}} />
                        </div>
                        <div className="text-center">
                            <i className="fab fa-bitcoin"></i>
                            &nbsp;
                            {this.state.raiseAmount}
                        </div>
                    </Modal>
                )}

                <Modal title="Game Over!" ref={m => this.endGameModal = m} onOK={this.leaveGame} hideDismiss={true}>
                    {game.players.map(p => (
                        <div>
                            <span>
                                {p.type === 'HUMAN' && <i className="fa fa-user"></i>}
                                {p.type === 'COMPUTER' && <i className="fa fa-desktop"></i>}
                                &nbsp;
                                {p.name}
                            </span>
                            &nbsp;&nbsp;
                            <span>
                                <i className="fab fa-bitcoin"></i>
                                &nbsp;
                                {p.chips}
                            </span>
                            &nbsp;&nbsp;
                            <span>
                                <i className="fa fa-trophy"></i>
                                &nbsp;
                                {p.handsWon}
                            </span>
                            &nbsp;&nbsp;
                            <span>
                                <i className="fa fa-shopping-cart"></i>
                                &nbsp;
                                {p.buyIns}
                            </span>
                        </div>
                    ))}
                </Modal>

                {hand && (
                    <Modal title="Winners" ref={m => this.winnersModal = m} hideDismiss={true}>
                        {hand.winners.map(w => (
                            <div>
                                {w.type === 'HUMAN' && <i className="fa fa-user"></i>}
                                {w.type === 'COMPUTER' && <i className="fa fa-desktop"></i>}
                                &nbsp;
                                {w.name}
                                &nbsp;-&nbsp;
                                Won {w.chipsWon} chips
                                {w.ranking && (' - ' + w.ranking)}
                            </div>
                        ))}
                    </Modal>
                )}

                <header>
                    <h1 className="text-center">
                        {game.title}
                        &nbsp;
                        (hand {game.handIndex}/{game.handsCount})
                    </h1>
                </header>

                <div className="page-body flex-row flex">
                    <div className="left-sidebar flex-column">
                        <div className="flex">
                            <PlayerList players={game.players} currentUser={this.selfGamePlayer} />
                        </div>

                        <div className="margin-b10 text-center">
                            <div>Small Blind: {(hand && hand.smallBlind) || game.initialSmallBlind}</div>
                            <div>Big Blind: {(hand && hand.bigBlind) || game.initialBigBlind}</div>
                        </div>
                    </div>

                    <div className="main-content flex flex-column">
                        <div className="container flex">
                            <Table game={game} hand={hand} currentUser={this.selfGamePlayer} />
                        </div>

                        {this.renderBottomMenu()}
                    </div>
                </div>

            </div>
        );
    }
}