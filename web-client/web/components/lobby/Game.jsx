class Game extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        const game = this.props.game;

        return (
            <div className={"game-item flex-column" + (!game.joinable ? ' disabled': '')} onClick={() => game.joinable && this.props.onJoin(game.id)}>
                <div className="title">{game.title}</div>
                <div className="players">{game.playerCount}/{game.seats}</div>
                <div className="author flex">Author: {game.author}</div>
                <div className="additional-data flex-row">
                    <div className="flex">Buy-In: {game.buyIn}</div>
                    <div className="flex">Hands: {game.handsCount}</div>
                </div>
                <div className="additional-data flex-row">
                    <div className="flex">Small Blind: {game.initialSmallBlind}</div>
                    <div className="flex">Big Blind: {game.initialBigBlind}</div>
                </div>
                {!game.fixedBlinds && (
                    <div className="additional-data flex-row">
                        <div className="flex">Additions: {game.blindAddition}</div>
                        <div className="flex">Addition Rounds: {game.maxTotalRoundsBlinds}</div>
                    </div>
                )}
            </div>
        );
    }
}