class Player extends React.Component {
    constructor(props) {
        super(props);
    }

    renderGamePlayer() {
        let {player, className} = this.props;

        return (
            <div className={'player ' + className}>
                <div className="player-info">
                    {player.type === 'HUMAN' && <i className="fa fa-user"></i>}
                    {player.type === 'COMPUTER' && <i className="fa fa-desktop"></i>}
                    &nbsp;
                    {player.name}
                </div>

                {player.ready && <i className="fa fa-check"></i>}
                {!player.ready && <i className="fa fa-hourglass-half"></i>}
            </div>
        );
    }

    renderHandPlayer() {
        let {player, className} = this.props;

        return (
            <div className={'player ' + className}>
                <div className="player-info">
                    {player.type === 'HUMAN' && <i className="fa fa-user"></i>}
                    {player.type === 'COMPUTER' && <i className="fa fa-desktop"></i>}
                    &nbsp;
                    {player.name}
                    &nbsp;
                    {player.state && (
                        <span className={'player-state' + (player.state === 'BIG_AND_DEALER'? ' player-state-xs': '')}>
                            {player.state === 'DEALER' && 'D'}
                            {player.state === 'BIG' && 'B'}
                            {player.state === 'SMALL' && 'S'}
                            {player.state === 'BIG_AND_DEALER' && 'B+D'}
                        </span>
                    )}
                </div>

                <div className="cards">
                    <Card card={player.firstCard} />
                    <Card card={player.secondCard} />
                </div>

                <div className="text-center">
                    <i className="fab fa-bitcoin"></i>
                    &nbsp;
                    {player.bet}
                </div>
            </div>
        );
    }

    render() {
        let {player, className} = this.props;

        if (!player.hasOwnProperty('bet')) {
            return this.renderGamePlayer();
        }
        return this.renderHandPlayer();
    }
}