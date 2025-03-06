var path = require('path');
var glob = require('glob');
var webpack = require('webpack');

module.exports = {
    entry: (function() {
        var entry = {};



        glob.sync('./src/jquery.qqface.js').forEach(function(name) {
            var key = name.replace('main.js', 'main.min.js');
            entry[key] = name;
            console.log(__dirname, name);
        });
        return entry;
    }()),
    module: {
        loaders: [{
            test: /\.html$/,
            loader: 'html-loader'
        }, {
            test: /\.css$/,
            loader: "style!css"
        }]
    },
    output: {
        // libraryTarget: 'amd',
        path: __dirname,
        filename: "[name]"
    },
    plugins: [
        // new webpack.HotModuleReplacementPlugin(),
        // new webpack.NoErrorsPlugin(),
        // new ExtractTextPlugin('styles.css')
        new webpack.optimize.UglifyJsPlugin()
    ]
};