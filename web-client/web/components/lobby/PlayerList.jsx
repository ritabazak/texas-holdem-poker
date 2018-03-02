class PlayerList extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let {currentUser, players} = this.props;

        return (
            <div className="player-list">
                <ul>
                    {players.map(p => (
                        <li key={p.name} className={p === currentUser? 'current-user': null}>
                            {p.type === 'HUMAN' && <i className="fa fa-user"></i>}
                            {p.type === 'COMPUTER' && <i className="fa fa-desktop"></i>}
                            &nbsp;
                            {p.name}
                        </li>
                    ))}
                </ul>
            </div>
        );
    }
}