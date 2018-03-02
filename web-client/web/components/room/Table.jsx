class Table extends React.Component {
    constructor(props) {
        super(props);

        this.state = {};
    }

    render() {
        let {game, hand, currentUser} = this.props;
        let players;

        if (hand) {
            players = hand.players;
        }
        else {
            players = game.players;
        }
        let {seats} = game;

        let emptySeats = 'x'.repeat(seats - players.length)
            .split('')
            .map((x, i) => <Seat key={i} className={'seat-' + (i + players.length) + '-' + seats} />);

        return (
            <div className="game-table">
                {players.map((p, i) => <Player player={p}
                                               key={p.name}
                                               className={'seat-' + i + '-' + seats + (p.current? ' current-player' : '')} />)}
                {emptySeats}

                {hand && (
                    <div className="table-center text-center">
                        <div>
                            {hand.communityCards.map(c => <Card card={c} />)}
                        </div>
                        <div>
                            <i className="fab fa-bitcoin"></i>
                            &nbsp;
                            {hand.pot}
                        </div>
                    </div>
                )}
            </div>
        );
    }
}