class GameList extends React.Component {
    constructor(props) {
        super(props);

        this.onFileChooserChange = this.onFileChooserChange.bind(this);
    }

    onFileChooserChange(e) {
        let fileInput = e.currentTarget;
        let file = fileInput.files[0];

        if (file) {
            const reader = new FileReader();
            reader.addEventListener('load', e => {
                this.props.onAdd(e.currentTarget.result);
                fileInput.value = '';
            });
            reader.readAsText(file);
        }
    }

    render() {
        let fileChooser;

        return (
            <div className="game-list flex-row flex-wrap">
                {this.props.games.map(g => <Game key={g.id} game={g} onJoin={this.props.onJoin} />)}
                <div className="game-item add-game" onClick={() => fileChooser.click()}>
                    <i className="fa fa-plus"></i>
                </div>

                <input type="file" accept=".xml"
                       style={{display: 'none'}}
                       ref={htmlInput => fileChooser = htmlInput}
                       onChange={this.onFileChooserChange} />
            </div>
        );
    }
}