require 'net/ssh'

desc "Create a distribution package"
task :package do
    sh "mvn package"
end

namespace "deploy" do
    host        = 'dlp'
    user        = 'root'
    options     = {:keys => '~/.ssh/id_dsa'}

    desc "Deploy the frontend webapp"
    task :frontend do
        remote_dir = "/home/darklord/www/forums/library/"
        sh "rsync -avz library-frontend/* #{host}:#{remote_dir}"
    end

    desc "Deploy the Library Extractor"
    task :extractor => [:package] do
        remote_dir = "/home/darklord/api"

        sh "rsync library-extractor/target/library-extractor-1.0-SNAPSHOT.jar #{host}:#{remote_dir}/library-extractor.jar"
    end

    desc "Deploy the Library REST API (non-rolling)"
    task :api => [:package] do
        remote_dir = "/home/darklord/api"

        sh "rsync library-query/target/library-query-1.0-SNAPSHOT.jar #{host}:#{remote_dir}/library-query.jar"

        Net::SSH.start(host, user, options) do |ssh|
            puts ssh.exec! 'supervisorctl restart library-api'
        end
    end
end