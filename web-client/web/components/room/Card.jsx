class Card extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let {card} = this.props;

        return (
            <img className="poker-card" src={'./common/images/cards/' + (card === '??'? 'blank' : card) + '.png'} />
        );
    }
}