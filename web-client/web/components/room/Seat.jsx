class Seat extends React.Component {
    constructor(props) {
        super(props);

        this.state = {};
    }

    render() {
        let {className} = this.props;

        return (
            <div className={'seat ' + className}>
            </div>
        );
    }
}