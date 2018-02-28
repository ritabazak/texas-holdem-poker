class PlayerList extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="player-list">
                <ul>
                    {this.props.players.map(p => (
                        <li key={p.id}>
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