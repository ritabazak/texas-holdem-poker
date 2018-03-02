class LoginPage extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            name: '',
            type: 'human'
        };

        this.playerTypeSelect = this.playerTypeSelect.bind(this);
        this.submitForm = this.submitForm.bind(this);
    }

    playerTypeSelect(e) {
        this.setState({type: e.target.value});
    }

    submitForm(e) {
        e.preventDefault();

        Http.post('api/login', this.state)
            .then(() => window.location.href = 'lobby.html')
            .catch(() => this.setState({ name: '' }));
    }

    render() {
        return (
            <div className="container">
                <h1 className="text-center">Welcome to the best Poker game ever!</h1>

                <form onSubmit={this.submitForm} style={{'margin-top': '50px'}}>
                    <div className="form-group">
                        <label>Name:</label>
                        <input type="text" className="form-control" value={this.state.name}
                               onChange={e => this.setState({name: e.target.value})} />
                    </div>

                    <div className="form-group">
                        <div className="form-check">
                            <label className="form-check-label">
                                <input className="form-check-input" type="radio"
                                       name="playerType" value="human" checked={this.state.type === 'human'}
                                       onChange={this.playerTypeSelect} />
                                <i className="fa fa-user"></i>
                                &nbsp;
                                Human
                            </label>
                        </div>

                        <div className="form-check">
                            <label className="form-check-label">
                                <input className="form-check-input" type="radio"
                                       name="playerType" value="computer" checked={this.state.type === 'computer'}
                                       onChange={this.playerTypeSelect} />
                                <i className="fa fa-desktop"></i>
                                &nbsp;
                                Computer
                            </label>
                        </div>
                    </div>

                    <button type="submit" className="btn btn-primary" disabled={!this.state.name} >
                        <i className="fa fa-user-plus"></i>
                        &nbsp;
                        Login
                    </button>
                </form>
            </div>

        );
    }
}