class PlayerList extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let {players, currentUser} = this.props;

        return (
            <table className="player-list">
                {players.map(p => (
                    <tr key={p.name} className={p === currentUser? 'current-user': null}>
                        <td className="flex">
                            {p.type === 'HUMAN' && <i className="fa fa-user"></i>}
                            {p.type === 'COMPUTER' && <i className="fa fa-desktop"></i>}
                            &nbsp;
                            {p.name}
                        </td>

                        <td>
                            <i className="fab fa-bitcoin"></i>
                            &nbsp;
                            {p.chips}
                        </td>

                        <td>
                            <i className="fa fa-trophy"></i>
                            &nbsp;
                            {p.handsWon}
                        </td>
                    </tr>
                ))}
            </table>
        );
    }
}