class PlayerList extends React.Component {
    constructor(props) {
        super(props);

        this.state = {};
    }

    render() {
        let {players, currentUser} = this.props;

        return (
            <ul className="player-list">
                {players.map(p => (
                    <li key={p.name} className={p === currentUser? 'current-user': null}>
                        <span>
                            {p.type === 'HUMAN' && <i className="fa fa-user"></i>}
                            {p.type === 'COMPUTER' && <i className="fa fa-desktop"></i>}
                            &nbsp;
                            {p.name}
                        </span>

                        <span>
                            <i className="fab fa-bitcoin"></i>
                            &nbsp;
                            {p.chips}
                        </span>

                        <span>
                            <i className="fa fa-trophy"></i>
                            &nbsp;
                            {p.handsWon}
                        </span>
                    </li>
                ))}
            </ul>
        );
    }
}